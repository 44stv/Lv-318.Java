import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ExcategoryModel } from '../../models/excategory.model';
import { NonExCategoryService } from '../../services/non-ex-category.service';
import { TransitsComponent } from '../transits/transits.component';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-non-ex-category',
  templateUrl: './non-ex-category.component.html',
  styleUrls: ['./non-ex-category.component.css']
})

export class NonExCategoryComponent implements OnInit {
  public list: Observable<ExcategoryModel[]>;

  serverURL = environment.serverURL + '/category/img?link=';

  displayedColumns = ['id', 'name', 'nextLevelCategory_name'];

  top: String;
  city: String;
  private sub: any;

  @ViewChild(TransitsComponent)
  private transitChild: TransitsComponent;

  constructor(private service: NonExCategoryService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.sub = this.route.params.forEach(params => {
      this.top = params['top'];
      this.city = params['city'];
      this.list = this.service.getByNames(this.city, this.top);
    });
  }
}
