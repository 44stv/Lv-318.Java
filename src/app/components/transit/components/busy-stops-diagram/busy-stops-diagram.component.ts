import { AfterViewInit, Component, Input } from '@angular/core';
import { Chart } from 'chart.js';

import { environment } from '../../../../../environments/environment';
import { DiagramService } from '../../../../services/diagram.service';
import { Stop } from '../../../../models/stop.model';

@Component({
  selector: 'app-busy-stops-diagram',
  templateUrl: './busy-stops-diagram.component.html',
  styleUrls: ['./busy-stops-diagram.component.css']
})
export class BusyStopsDiagramComponent implements AfterViewInit {
  @Input() id: number;
  @Input() stopList: Stop[];
  lineChart1;

  constructor(private diagramService: DiagramService) {
  }

  ngAfterViewInit(): void {
    this.diagramService.getResults(environment.serverURL + '/feedback/byStop/' + this.id + '?stop-list=')
      .subscribe(res => {
        const CHART = document.getElementById('lineChart1');

        this.lineChart1 = new Chart(CHART, {
          type: 'line',
          data: {
            labels: Object.keys(res).map(data => data.substring(data.indexOf('=', 2) + 11, data.indexOf(', lat'))),
            datasets: [{
              fill: true,
              lineTension: 0.5,
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
