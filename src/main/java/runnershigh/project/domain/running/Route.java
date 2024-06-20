package runnershigh.project.domain.running;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer latitude;
    private Integer longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "runningRoute_id")
    private RunningRoute runningRoute;

    public Route(Integer latitude, Integer longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //    public static Route routeDTOToEntity(RouteDTO routeDTO) {
//        Route route = new Route();
//        route.setLatitude(routeDTO.getLatitude());
//        route.setLongitude(routeDTO.getLongitude());
//
//        return route;
//    }



}
