import {AfterViewInit, Component, Input} from '@angular/core';
import {Chart} from 'chart.js';

import {DiagramService} from '../../../../services/diagram.service';
import {environment} from '../../../../../environments/environment';


@Component({
  selector: 'app-busy-hours-diagram',
  templateUrl: './busy-hours-diagram.component.html',
  styleUrls: ['./busy-hours-diagram.component.css']
})
export class BusyHoursDiagramComponent implements AfterViewInit {
  @Input() id: number;

  constructor(private diagramService: DiagramService) {
  }

  ngAfterViewInit(): void {
    this.diagramService.getResults(environment.serverURL + '/feedback/byHour/' + this.id)
      .subscribe(res => {
        const CHART = document.getElementById('lineChart');

        const lineChart = new Chart(CHART, {
          type: 'line',
          data: {
            labels: Object.keys(res).map(data => data.concat(':00')),
            datasets: [{
              fill: true,
              lineTension: 0.6,
              backgroundColor: 'rgba(75,192,192,0.4)',
              borderColor: 'rgba(75,192,192,1)',
              borderCapStyle: 'butt',
              borderDash: [],
              borderDashOffset: 0.0,
              borderJointStyle: 'miter',
              data: Object.values(res).concat([0, 100]).map(data => (<number>data).toPrecision(3))
            }]
          },
          options: {
            legend: {
              display: false
            }
          }
        });
      });
  }
}
