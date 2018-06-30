import { Component, OnInit, ViewChild } from '@angular/core';
import { GlobalSearchService } from '../../services/global-search.service';
import { Transit } from '../../models/transit.model';
import { MatPaginator, MatTableDataSource } from '@angular/material';
import { Location } from '@angular/common';

@Component({
  selector: 'app-global-search',
  templateUrl: './global-search.component.html',
  styleUrls: ['./global-search.component.css']
})
export class GlobalSearchComponent implements OnInit {
  empty = true;
  emptyTransit = false;

  transits: Transit [];
  searchValue = '';
  displayedColumns = ['id', 'name', 'categoryId', 'routeName'];
  dataSource = new MatTableDataSource();

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private globalSearchService: GlobalSearchService,
              private location: Location) {
  }

  ngOnInit() {
    this.empty = true;
    this.emptyTransit = false;
    this.getResults();
  }


  getResults(): void {
    this.globalSearchService.getResults().subscribe(transits => {
      this.dataSource.data = transits;
      if (!(transits.length === 0)) {
        this.emptyTransit = true;
        this.empty = false;
      }
      if (transits.length === 0) {
        this.emptyTransit = false;
        this.empty = true;
      }
      this.dataSource.paginator = this.paginator;
    });
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim();
    this.dataSource.filter = filterValue;
    this.dataSource.filterPredicate = (data, filter) =>
      JSON.stringify(data).includes(filter);
  }

  gotBack(): void {
    this.location.back();
  }
}
