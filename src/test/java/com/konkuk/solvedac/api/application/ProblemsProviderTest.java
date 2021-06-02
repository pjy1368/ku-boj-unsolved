package com.konkuk.solvedac.api.application;

import static com.konkuk.solvedac.api.Constants.ALL_PROBLEMS_URL;
import static com.konkuk.solvedac.api.Constants.SERVER_URL;
import static com.konkuk.solvedac.api.Constants.SOLVED_PROBLEMS_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.solvedac.problem.dto.ProblemInfoResponse;
import com.konkuk.solvedac.problem.dto.ProblemInfoResponses;
import com.konkuk.solvedac.problem.dto.ProblemResultResponse;
import com.konkuk.solvedac.problem.dto.ProblemsResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(ProblemsProvider.class)
class ProblemsProviderTest {

    @Autowired
    private ProblemsProvider problemsProvider;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getSolvedProblem() throws JsonProcessingException {
        final String expectedMemberId = "test";
        final List<ProblemInfoResponse> problems = Collections.singletonList(
            new ProblemInfoResponse(1000L, 1L, (short) 1, (short) 1, "A+B", 126343L, 2.2986)
        );
        final ProblemResultResponse result = new ProblemResultResponse(1L, 1L, problems);
        final ProblemsResponse problemsResponse = new ProblemsResponse(true, result);
        final String expectResult = mapper.writeValueAsString(problemsResponse);

        this.mockServer.expect(requestTo(SERVER_URL + SOLVED_PROBLEMS_URL + expectedMemberId))
            .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        final ProblemInfoResponses actual = problemsProvider.getSolvedProblems(expectedMemberId);
        assertThat(actual.getProblemInfoResponses()).hasSize(1);
        assertThat(actual.getProblemInfoResponses().get(0).getId()).isEqualTo(1000L);
    }

    @Test
    void getSolvedProblems() throws JsonProcessingException {
        final String expectedMemberId = "test2";
        final List<ProblemInfoResponse> problems = Arrays.asList(
            new ProblemInfoResponse(1000L, 1L, (short) 1, (short) 1, "A+B", 126343L, 2.2986),
            new ProblemInfoResponse(1001L, 1L, (short) 1, (short) 1, "A+B+C", 12332L, 2.3)
        );
        final ProblemResultResponse result = new ProblemResultResponse(2L, 1L, problems);
        final ProblemsResponse problemsResponse = new ProblemsResponse(true, result);
        final String expectResult = mapper.writeValueAsString(problemsResponse);

        this.mockServer.expect(requestTo(SERVER_URL + SOLVED_PROBLEMS_URL + expectedMemberId))
            .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        final ProblemInfoResponses actual = problemsProvider.getSolvedProblems(expectedMemberId);
        assertThat(actual.getProblemInfoResponses()).hasSize(2);
        assertThat(actual.getProblemInfoResponses().get(0).getId()).isEqualTo(1000L);
        assertThat(actual.getProblemInfoResponses().get(1).getId()).isEqualTo(1001L);
    }

    @Test
    void getAllProblems() throws JsonProcessingException {
        final List<ProblemInfoResponse> problems = Arrays.asList(
            new ProblemInfoResponse(1000L, 1L, (short) 1, (short) 1, "A+B", 126343L, 2.2986),
            new ProblemInfoResponse(1001L, 1L, (short) 1, (short) 1, "A+B+C", 12332L, 2.3)
        );
        final ProblemResultResponse result = new ProblemResultResponse(2L, 1L, problems);
        final ProblemsResponse problemsResponse = new ProblemsResponse(true, result);
        final String expectResult = mapper.writeValueAsString(problemsResponse);

        this.mockServer.expect(requestTo(SERVER_URL + ALL_PROBLEMS_URL))
            .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        final ProblemInfoResponses actual = problemsProvider.getAllProblems();
        assertThat(actual.getProblemInfoResponses()).hasSize(2);
        assertThat(actual.getProblemInfoResponses().get(0).getId()).isEqualTo(1000L);
        assertThat(actual.getProblemInfoResponses().get(1).getId()).isEqualTo(1001L);
    }
}