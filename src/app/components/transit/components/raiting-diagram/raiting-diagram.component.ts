import {AfterViewInit, Component, Input} from '@angular/core';
import {Chart} from 'chart.js';

import {DiagramService} from '../../../../services/diagram.service';
import {environment} from '../../../../../environments/environment';

@Component({
  selector: 'app-raiting-diagram',
  templateUrl: './raiting-diagram.component.html',
  styleUrls: ['./raiting-diagram.component.css']
})
export class RaitingDiagramComponent implements AfterViewInit {

  @Input() id: number;

  constructor(private diagramService: DiagramService) {
  }

  ngAfterViewInit(): void {
    this.diagramService.getResults(environment.serverURL + '/feedback/accepterMap/' + this.id)
      .subscribe(res => {
        const CHART = document.getElementById('doughnut');

        const doughnut = new Chart(CHART, {
          type: 'doughnut',
          data: {
            labels: Object.keys(res),
            datasets: [{
              label: 'Rating diagram',
              fill: true,
              backgroundColor: ['rgba(0, 200, 0, 0.7)', 'rgba(255, 26, 26, 0.7)', 'rgba(255, 255, 26, 0.7)'],
              borderColor: 'gray',
              clickable: false,
              borderCapStyle: 'butt',
              borderDash: [],
              borderDashOffset: 0.0,
              data: Object.values(res).map(data => (<number>data).toPrecision(3))
            }]
          },
          options: {
            cutoutPercentage: 75
          }
        });
      });
  }
}
