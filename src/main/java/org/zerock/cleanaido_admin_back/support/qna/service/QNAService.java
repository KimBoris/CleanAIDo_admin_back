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
import org.zerock.cleanaido_admin_back.support.qna.dto.AnswerDTO;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionReadDTO;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionListDTO;
import org.zerock.cleanaido_admin_back.support.qna.entity.Answer;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class QNAService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;


    public PageResponseDTO<QuestionListDTO> listQuestion(PageRequestDTO pageRequestDTO) {
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
    }


    public QuestionReadDTO read(Long qno) {

        return questionRepository.getQuestion(qno);
    }

    @Transactional
    public void saveAnswer(String answerText, Long qno) {

        Question question = Question.builder()
                .qno(qno)
                .build();

        // Answer 객체를 생성합니다.
        Answer answer = Answer.builder()
                .answerText(answerText)
                .question(question) // DTO에서 엔티티로 변환
                .build();

        // Answer 객체 저장
        answerRepository.save(answer);
    }
}
