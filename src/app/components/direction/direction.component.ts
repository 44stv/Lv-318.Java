import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-direction',
  templateUrl: './direction.component.html',
  styleUrls: ['./direction.component.css']
})
export class DirectionComponent implements OnInit {
  public lat: Number = 49.873692695218;
  public lng: Number = 24.060354471207;
  public zoom: Number = 20;
  public waypoints: object = [
    {
      location: {lat: 49.86952258602, lng: 24.056770317288},
      stopover: true,
    },
    {
      location: {lat: 49.86516499674, lng: 24.050964415073},
      stopover: true,
    },{
      location:{lat: 49.865291595458984, lng: 24.043611526489258},
      stopover: true,
    }];

  public dir = undefined;

  getDirection() {
    this.dir = {
      origin: { lat: 49.873692695218, lng: 24.060354471207 },
      destination: { lat: 49.86345672607422, lng: 24.032636642456055 }
    }
  }

  constructor() { }

  ngOnInit() {
  }


}
