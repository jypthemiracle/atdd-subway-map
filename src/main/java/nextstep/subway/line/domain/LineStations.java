package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class LineStations {

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_line_station_to_line"))
	private List<LineStation> lineStations = new ArrayList<>();

	public LineStation add(LineStation lineStation) {
		lineStations.stream()
			.filter(station -> station.comparePreStationId(lineStation))
			.findFirst()
			.ifPresent(station -> station.updatePreStationTo(lineStation));
		lineStations.add(lineStation);
		return lineStation;
	}
}
