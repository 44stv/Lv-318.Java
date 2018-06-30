import {Component, Input, OnInit} from '@angular/core';
import {DiagramService} from '../../../../services/diagram.service';
import {environment} from '../../../../../environments/environment';

@Component({
  selector: 'app-average-rate',
  templateUrl: './average-rate.component.html',
  styleUrls: ['./average-rate.component.css']
})
export class AverageRateComponent implements OnInit {

  @Input() id: number;

  averageRate;

  constructor(private diagramService: DiagramService) {
  }

  ngOnInit() {
    this.diagramService.getResults(environment.serverURL + '/feedback/rate/' + this.id)
      .subscribe(res => {
        this.averageRate = (<number>res).toPrecision(3);
      });
  }

}
