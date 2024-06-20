package runnershigh.project.domain.running;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RunningRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer km;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "runningRecord_id")
    private RunningRecord runningRecord;

    @OneToMany(mappedBy = "runningRoute", cascade = CascadeType.ALL)
    private List<Route> routeList = new ArrayList<>();

    public RunningRoute(Integer km) {
        this.km = km;
    }

    public RunningRoute(Integer km, List<Route> routeList) {
        this.km = km;
        for (Route route : routeList) {
            route.setRunningRoute(this);
            addRouteList(route);
        }
//        this.routeList = routeList;
    }

    public void addRouteList(Route route) {
        route.setRunningRoute(this);
        this.routeList.add(route);
    }


//    public static RunningRoute runningRouteDTOToEntity(RunningRouteDTO runningRouteDTO) {
//
//        RunningRoute runningRoute = new RunningRoute();
//        runningRoute.setKm(runningRouteDTO.getKm());
//
//        return runningRoute;
//    }


}
