package com.project.doongdoong.domain.analysis.adapter.in.dto;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.question.domain.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class AnalysisListResponseDto {
    private int pageNumber;
    private int totalPage;
    private List<AnalysisResponseDto> analysisResponseDtoList;

    @Builder
    private AnalysisListResponseDto(int pageNumber, int totalPage, List<AnalysisResponseDto> analysisResponseDtoList) {
        this.pageNumber = pageNumber;
        this.totalPage = totalPage;
        this.analysisResponseDtoList = analysisResponseDtoList;
    }

    public static AnalysisListResponseDto of(Page<Analysis> analysisPages, Map<Long, List<Question>> questionMap, DateTimeFormatter formatter) {
        return AnalysisListResponseDto.builder()
                .pageNumber(analysisPages.getNumber() + 1)
                .totalPage(analysisPages.getTotalPages())
                .analysisResponseDtoList(
                        analysisPages.getContent()
                                .stream()
                                .map(analysis -> {
                                    return AnalysisResponseDto.builder()
                                            .analysisId(analysis.getId())
                                            .feelingState(analysis.getFeelingState())
                                            .time(analysis.getAnalyzedDate().format(formatter))
                                            .questionContent(questionMap.get(analysis.getId())
                                                    .stream()
                                                    .map(question -> question.getQuestionContent().getText())
                                                    .toList())
                                            .build();
                                }).toList()
                )
                .build();
    }


}
