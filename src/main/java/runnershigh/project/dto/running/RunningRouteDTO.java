package runnershigh.project.dto.running;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import runnershigh.project.domain.running.Route;
import runnershigh.project.domain.running.RunningRoute;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RunningRouteDTO {

    private Integer km;
    private List<RouteDTO> routeDTOList;

    public RunningRouteDTO(RunningRoute runningRoute) {
        this.km = runningRoute.getKm();
        routeDTOList = new ArrayList<>();
        List<Route> routeList = runningRoute.getRouteList();
        for (Route route : routeList) {
            RouteDTO routeDTO = new RouteDTO(route.getLatitude(), route.getLongitude());
            this.routeDTOList.add(routeDTO);
        }
    }
}
