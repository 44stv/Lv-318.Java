import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MapsService} from '../../services/maps.service';
import {ActivatedRoute} from '@angular/router';
import {environment} from '../../../environments/environment';
import {WaypointModel, Location} from '../../models/waypoint.model';

@Component({
  selector: 'app-maps',
  templateUrl: './maps.component.html',
  styleUrls: ['./maps.component.css']
})
export class MapsComponent implements OnInit {
  public transitId: string;
  public routeDirection: string;
  public latStatic: Number = 49.84012222290039;
  public lngStatic: Number = 24.028803095222;
  public markerOneLat;
  public markerOneLng;
  public markerOneName;
  public markerTwoLat;
  public markerTwoLng;
  public markerTwoName;
  public icon = 'http://localhost:8080/category/img?link=static/bus-stop.png';
  public direction = undefined;
  public travelMode = 'DRIVING';
  private serviceUrl = environment.serverURL + '/stop';
  public points: any;
  public markers: MarkerModel[];
  public zoom = 13;
  public renderOptions = {
    draggable: false,
    suppressMarkers: true,
    polylineOptions: {
      strokeColor: '#42fc0f'
    }
  };
  public waypoints;

  public markerOptions = {
    origin: {
      icon: null,
      opacity: 0,
      scaledSize: 0.1,
    },
    destination: {
      icon: null,
      opacity: 0,
      scaledSize: 0.1,
    },
  };

  constructor(private route: ActivatedRoute, private service: MapsService) {
  }

  ngOnInit() {
    this.route.params.forEach(param => {
      this.transitId = param['id'];
    });
    this.service.getForwardDirection(this.transitId, 'forward').subscribe(points => this.points = points);
  }

  setDirectionAndRefresh(routeDirection) {
    this.routeDirection = routeDirection;
    console.log(this.routeDirection);
    this.service.getForwardDirection(this.transitId, this.routeDirection).subscribe(points => this.points = points);
    this.mapClicked();
  }

  mapClicked() {
    this.markers = new Array(this.points.length);
    for (let i = 0; i < this.points.length; i++) {
      let marker: MarkerModel = new MarkerModel();
      marker.name = this.points[i].street;
      marker.lat = this.points[i].lat;
      marker.lng = this.points[i].lng;
      marker.draggable = false;
      this.markers[i] = marker;
    }
    this.direction = {
      origin: {lat: this.points[0].lat, lng: this.points[0].lng},
      destination: {lat: this.points[this.points.length - 1].lat, lng: this.points[this.points.length - 1].lng}
    };
    this.waypoints = new Array(this.points.length - 2);
    for (let i = 1; i < this.points.length - 1; i++) {
      let waypoint: WaypointModel = new WaypointModel();
      let location: Location = new Location();
      location.lat = this.points[i].lat;
      location.lng = this.points[i].lng;
      waypoint.location = location;
      waypoint.stopover = true;
      this.waypoints[i - 1] = waypoint;
    }
    this.waypoints.forEach(point => console.log(point));
  }

  clickedMarker(marker) {
    if (this.markerOneLat === undefined) {
      this.markerOneName = marker.name;
      this.markerOneLat = marker.lat;
      this.markerOneLng = marker.lng;
    } else {
      this.markerTwoName = marker.name;
      this.markerTwoLat = marker.lat;
      this.markerTwoLng = marker.lng;
    }
  }
}

export class MarkerModel {
  name: String;
  lat: number;
  lng: number;
  draggable: boolean;
}
