package nextstep.subway.line.acceptance;

import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.acceptance.step.LineStationRemoveAcceptanceStep;
import nextstep.subway.station.dto.StationResponse;

public class LineStationRemoveAcceptanceTest extends AcceptanceTest {

	private ExtractableResponse<Response> createdLineResponse;

	private ExtractableResponse<Response> firstStationResponse;
	private ExtractableResponse<Response> secondStationResponse;
	private ExtractableResponse<Response> thirdStationResponse;
	private ExtractableResponse<Response> fourthStationResponse;

	private ExtractableResponse<Response> firstLineStationResponse;
	private ExtractableResponse<Response> secondLineStationResponse;
	private ExtractableResponse<Response> thirdLineStationResponse;

	private Long lineId;
	private Long firstStationId;
	private Long secondStationId;
	private Long thirdStationid;
	private Long fourthStationId;

	private String firstStationName;
	private String secondStationName;
	private String thirdStationName;

	@BeforeEach
	public void setUp() {
		super.setUp();

		createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
		firstStationResponse = 지하철역_등록되어_있음("강남역");
		secondStationResponse = 지하철역_등록되어_있음("역삼역");
		thirdStationResponse = 지하철역_등록되어_있음("선릉역");
		fourthStationResponse = 지하철역_등록되어_있음("삼성역");

		lineId = createdLineResponse.as(LineResponse.class).getId();
		firstStationId = firstStationResponse.as(StationResponse.class).getId();
		secondStationId = secondStationResponse.as(StationResponse.class).getId();
		thirdStationid = thirdStationResponse.as(StationResponse.class).getId();
		fourthStationId = fourthStationResponse.as(StationResponse.class).getId();

		firstStationName = firstStationResponse.as(StationResponse.class).getName();
		secondStationName = secondStationResponse.as(StationResponse.class).getName();
		thirdStationName = thirdStationResponse.as(StationResponse.class).getName();

		firstLineStationResponse = 노선에_지하철역_첫번째_등록(firstStationId, lineId);
		secondLineStationResponse = 노선에_지하철역_추가로_등록(firstStationId, secondStationId, lineId);
		thirdLineStationResponse = 노선에_지하철역_추가로_등록(secondStationId, thirdStationid, lineId);
	}

	@DisplayName("지하철 노선에 등록된 마지막 지하철역을 제외한다.")
	@Test
	void 노선의_마지막_역을_삭제한다() {
		// when
		// 지하철 노선의 마지막에 지하철역 제외 요청
		ExtractableResponse<Response> request = LineStationRemoveAcceptanceStep.노선에_지하철역_제외(lineId, thirdStationid);

		// then
		// 지하철 노선에 지하철역 제외됨
		// TODO: 컨트롤러 단에서는 200 반환이 되는 것으로만 믿으면 충분한지 궁금하다.
		assertThat(request.statusCode()).isEqualTo(HttpStatus.OK.value());

		// when
		// 지하철 노선 상세정보 조회 요청
		ExtractableResponse<Response> response = 노선정보_확인_요청(lineId);

		// then
		// 지하철 노선에 지하철역 제외 확인됨
		assertThat(response.body().asString().contains(thirdStationName)).isEqualTo(false);

		// and
		// 지하철 노선에 지하철역 순서 정렬됨
		List<Long> stationIds = Arrays.asList(firstStationId, secondStationId);
		assertThat(LineStationRemoveAcceptanceStep.지하철_노선에_지하철역_순서_정렬됨(response, stationIds))
			.isEqualTo(true);
	}

	@DisplayName("지하철 노선에 등록된 중간 지하철역을 제외한다.")
	@Test
	void 노선의_중간_역을_삭제한다() {
		// when
		// 지하철 노선의 중간 지하철역 제외 요청
		ExtractableResponse<Response> request = LineStationRemoveAcceptanceStep.노선에_지하철역_제외(lineId, secondStationId);

		// then
		// 지하철 노선에 지하철역 제외됨
		assertThat(request.statusCode()).isEqualTo(HttpStatus.OK.value());

		// when
		// 지하철 노선 상세정보 조회 요청
		ExtractableResponse<Response> response = 노선정보_확인_요청(lineId);

		// then
		// 지하철 노선에 지하철역 제외 확인됨
		assertThat(response.body().asString().contains(secondStationName)).isEqualTo(false);

		// and
		// 지하철 노선에 지하철역 순서 정렬됨
		List<Long> stationIds = Arrays.asList(firstStationId, thirdStationid);
		assertThat(LineStationRemoveAcceptanceStep.지하철_노선에_지하철역_순서_정렬됨(createdLineResponse, stationIds))
			.isEqualTo(true);

		// and
		// 지하철 노선에 삭제된 지하철역이 연결 번호로서 존재하지 않음
		assertThat(LineStationRemoveAcceptanceStep.지하철_노선에_삭제된_역이_이전번호로_존재하지_않음(response, secondStationId));
	}

	@DisplayName("지하철 노선에 등록된 첫 번째 지하철역을 제외한다.")
	@Test
	void 노선의_첫번째_역을_삭제한다() {
		// when
		// 지하철 노선의 중간 지하철역 제외 요청
		ExtractableResponse<Response> request = LineStationRemoveAcceptanceStep.노선에_지하철역_제외(lineId, firstStationId);

		// then
		// 지하철 노선에 지하철역 제외됨
		assertThat(request.statusCode()).isEqualTo(HttpStatus.OK.value());

		// when
		// 지하철 노선 상세정보 조회 요청
		ExtractableResponse<Response> response = 노선정보_확인_요청(lineId);

		// then
		// 지하철 노선에 지하철역 제외 확인됨
		assertThat(response.body().asString().contains(firstStationName)).isEqualTo(false);

		// and
		// 지하철 노선에 지하철역 순서 정렬됨
		List<Long> stationIds = Arrays.asList(secondStationId, thirdStationid);
		assertThat(LineStationRemoveAcceptanceStep.지하철_노선에_지하철역_순서_정렬됨(createdLineResponse, stationIds))
			.isEqualTo(true);

		// and
		// 지하철 노선에 삭제된 지하철역이 연결 번호로서 존재하지 않음
		assertThat(LineStationRemoveAcceptanceStep.지하철_노선에_삭제된_역이_이전번호로_존재하지_않음(response, firstStationId));
	}

	@DisplayName("지하철 노선에 등록되지 않은 역을 삭제하려고 할 경우 예외를 반환한다.")
	@Test
	void 노선에_없는_역을_삭제하려고_하면_오류를_반환한다() {
		// when
		// 지하철 노선에 등록되지 않은 역 제외 요청
		ExtractableResponse<Response> request = LineStationRemoveAcceptanceStep.노선에_지하철역_제외(lineId, fourthStationId);

		// then
		// 지하철 노선에 지하철역 제외 실패됨
		assertThat(request.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}