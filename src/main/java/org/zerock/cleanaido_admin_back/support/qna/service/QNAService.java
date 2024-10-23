package org.zerock.cleanaido_admin_back.support.qna.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.qna.Repository.AnswerRepository;
import org.zerock.cleanaido_admin_back.support.qna.Repository.QuestionRepository;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionReadDTO;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionListDTO;
import org.zerock.cleanaido_admin_back.support.qna.entity.Answer;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class QNAService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public PageResponseDTO<QuestionListDTO> listQuestion(PageRequestDTO pageRequestDTO) {
        try {
            Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
            Page<Question> questionPage = questionRepository.list(pageable);

            List<QuestionListDTO> dtoList = questionPage.getContent().stream()
                    .map(question -> QuestionListDTO.builder()
                            .qno(question.getQno())
                            .title(question.getTitle())
                            .writer(question.getWriter())
                            .answered(question.isAnswered())
                            .build()).collect(Collectors.toList());

            return new PageResponseDTO<>(dtoList, pageRequestDTO, questionPage.getTotalElements());
        } catch (IllegalArgumentException ex) {
            log.error("페이지 번호는 1 이상이어야 합니다: {}", ex.getMessage());
            throw new IllegalArgumentException("페이지 번호는 1 이상이어야 합니다.");
        } catch (Exception ex) {
            log.error("질문 목록을 불러오는 중 오류가 발생했습니다: {}", ex.getMessage());
            throw new RuntimeException("질문 목록을 불러오는 중 오류가 발생했습니다.");
        }
    }

    public QuestionReadDTO read(Long qno) {
        try {
            return questionRepository.getQuestion(qno);
        } catch (Exception ex) {
            log.error("해당 질문을 불러오는 중 오류가 발생했습니다: {}", ex.getMessage());
            throw new RuntimeException("해당 질문을 불러오는 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    public void saveAnswer(String answerText, Long qno) {
        try {
            // 데이터베이스에서 해당 qno에 해당하는 Question 엔티티를 조회합니다.
            Question question = questionRepository.findById(qno)
                    .orElseThrow(() -> new IllegalArgumentException("해당 qno에 해당하는 질문을 찾을 수 없습니다."));

            // Question의 answered 속성을 true로 변경합니다.
            question.setAnswered(true);

            // Answer 객체를 생성합니다.
            Answer answer = Answer.builder()
                    .answerText(answerText)
                    .question(question) // 실제 데이터베이스에 있는 question을 참조
                    .build();

            // Answer 객체 저장
            answerRepository.save(answer);

            // Question 객체 저장 (변경된 answered 속성 저장)
            questionRepository.save(question);

        } catch (IllegalArgumentException ex) {
            log.error("답변을 저장하는 중 오류가 발생했습니다: {}", ex.getMessage());
            throw new IllegalArgumentException("해당 질문을 찾을 수 없습니다.");
        } catch (Exception ex) {
            log.error("답변 저장 중 문제가 발생했습니다: {}", ex.getMessage());
            throw new RuntimeException("답변 저장 중 문제가 발생했습니다.");
        }
    }

    @Transactional
    public void updateAnswer(String answerText, Long qno) {
        try {
            Answer answer = answerRepository.findAnswerByQno(qno);

            if (answer == null) {
                throw new IllegalArgumentException("해당 qno에 대한 답변을 찾을 수 없습니다.");
            }

            answer.setAnswerText(answerText);
        } catch (IllegalArgumentException ex) {
            log.error("답변을 수정하는 중 오류가 발생했습니다: {}", ex.getMessage());
            throw new IllegalArgumentException("해당 답변을 찾을 수 없습니다.");
        } catch (Exception ex) {
            log.error("답변 수정 중 문제가 발생했습니다: {}", ex.getMessage());
            throw new RuntimeException("답변 수정 중 문제가 발생했습니다.");
        }
    }
}
