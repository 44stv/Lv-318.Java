import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import 'hammerjs';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {HttpClientModule, HttpClient} from '@angular/common/http';
import {AppRoutingModule} from 'src/app/app-routing.module';
import {ExcategoryComponent} from './components/excategory/excategory.component';
import {TranslateModule, TranslateLoader} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';

import {
  MatFormField,
  MatAutocompleteModule,
  MatBadgeModule,
  MatBottomSheetModule,
  MatButtonModule,
  MatButtonToggleModule,
  MatCardModule,
  MatCheckboxModule,
  MatChipsModule,
  MatDatepickerModule,
  MatDialogModule,
  MatDividerModule,
  MatExpansionModule, MatFormFieldModule,
  MatGridListModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatProgressBarModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatRippleModule,
  MatSelectModule,
  MatSidenavModule,
  MatSliderModule,
  MatSlideToggleModule,
  MatSnackBarModule,
  MatSortModule,
  MatStepperModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule,
  MatTooltipModule,
  MatTreeModule,
  MatOptionModule
} from '@angular/material';

import {FormsModule} from '@angular/forms';
import {ExcategoryService} from './services/excategory.service';
import {MenuComponent} from './components/menu/menu.component';
import {NonExCategoryComponent} from './components/non-ex-category/non-ex-category.component';
import {TransitsComponent} from './components/transits/transits.component';
import {MainComponent} from './components/main/main.component';
import {SlideshowModule} from 'ng-simple-slideshow';
import {FeedbackCriteriaComponent} from './components/feedback-criteria/feedback-criteria.component';
import {AddFeedbackCriteriaComponent} from './components/feedback-criteria/addFC/add-feedback-criteria.component';
import {AddUserComponent} from './components/add-user/add-user.component';
import {StopsComponent} from "./components/stops/stops.component";

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}


@NgModule({
  declarations: [
    AppComponent,
    ExcategoryComponent,
    MenuComponent,
    NonExCategoryComponent,
    TransitsComponent,
    MainComponent,
    FeedbackCriteriaComponent,
    AddFeedbackCriteriaComponent,
    AddUserComponent,
    StopsComponent
  ],
  exports: [
    MatAutocompleteModule,
    MatBadgeModule,
    MatBottomSheetModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatStepperModule,
    MatDatepickerModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatRippleModule,
    MatSelectModule,
    MatSidenavModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
    MatTreeModule,
    MatFormField,
    MatOptionModule,
    MatSelectModule
  ],
  imports: [
    BrowserModule, BrowserAnimationsModule, MatSidenavModule,
    HttpClientModule,
    AppRoutingModule,
    MatTableModule,
    MatMenuModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatToolbarModule,
    SlideshowModule,
    HttpClientModule,
    MatOptionModule,
    MatSelectModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [ExcategoryService],
  bootstrap: [AppComponent]
})
export class AppModule {
}