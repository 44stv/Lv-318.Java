import { Component, OnInit, ViewChild } from '@angular/core';
import { TransitService } from '../../services/transit.service';
import { Convert, Transit } from '../../models/transit.model';
import { ActivatedRoute } from '@angular/router';
import { MatSort, MatTableDataSource, MatPaginator } from '@angular/material';

@Component({
  selector: 'app-transits',
  templateUrl: './transits.component.html',
  styleUrls: ['./transits.component.css']
})
export class TransitsComponent implements OnInit {

  categoryId: number;
  private sub: any;

  transit = new Transit();

  dataSource: MatTableDataSource<Transit> = new MatTableDataSource();

  displayedColumns = ['id', 'name', 'categoryId', 'routeName'];

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private transitService: TransitService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.getAllTransits();
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  getAllTransits(): void {
    this.sub = this.route.params.forEach(params => {
      if (params['id'] !== undefined) {
        this.getAllByCategoryId(params['id']);
      }
      if (params['id'] === undefined) {
        this.getAllByNextLevelCategoryName(params['city']);
      }
    });
  }

  onSubmit() {
    this.transitService.addTransit(this.transit)
      .subscribe(res => console.log(res));
    alert('Transit added: ' + Convert.transitToJson(this.transit));
  }

  getAllByCategoryId(categoryId: number) {
    this.transitService.getTransitsByCategoryId(categoryId)
      .subscribe(transits => this.dataSource.data = transits);
  }

  getAllByNextLevelCategoryName(categoryName: string) {
    this.transitService.getTransitsByNextLevelCategoryName(categoryName)
      .subscribe(transits => this.dataSource.data = transits);
  }
  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
