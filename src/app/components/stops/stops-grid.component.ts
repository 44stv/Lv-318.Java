import {Component, OnInit} from '@angular/core';
import {Stop} from '../../models/stop.model';
import {ActivatedRoute} from '@angular/router';
import {StopService} from '../../services/stop.service';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-stops-grid',
  templateUrl: './stops-grid.component.html',
  styleUrls: ['./stops-grid.component.css']
})
export class StopsGridComponent implements OnInit {

  checkedItems: boolean[];
  private sub: any;
  public idTransit: number;
  stopsList: Observable<Stop[]>;
  stopArray: Stop[] = [];
  public selectedStops: Stop[] = [];

  constructor(private stopService: StopService, private route: ActivatedRoute) {
  }


  ngOnInit() {
    this.sub = this.route.params.forEach(params => {
      this.idTransit = params['id'];
    });
    this.stopsList = this.stopService.getStopsByTransitId(this.idTransit);
    this.stopsList.subscribe(stopArray =>
      this.stopArray = stopArray);
    this.checkedItems = new Array(this.stopArray.length);
    this.selectStops();

  }

  public selectStops() {
    for (let i = 0; i < this.checkedItems.length; i++) {
      if (this.checkedItems[i] === true && !this.selectedStops.includes(this.stopArray[i], 0)) {
        this.selectedStops.push(this.stopArray[i]);
      }
      if (this.checkedItems[i] === false && this.selectedStops.includes(this.stopArray[i], 0)) {
        this.selectedStops.splice(this.selectedStops.indexOf(this.stopArray[i]), 1);
      }
    }


    console.log(this.selectedStops);
  }

}

