import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import 'hammerjs';
import { AuthService } from './services/auth/auth.service';
import { AppComponent } from './app.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { ExcategoryComponent } from './components/excategory/excategory.component';
import { HttpModule } from '@angular/http';


import {
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
  MatExpansionModule,
  MatFormFieldModule,
  MatGridListModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatNativeDateModule,
  MatOptionModule,
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
} from '@angular/material';

import { FormsModule } from '@angular/forms';
import { ExcategoryService } from './services/excategory.service';
import { MenuComponent } from './components/menu/menu.component';
import { NonExCategoryComponent } from './components/non-ex-category/non-ex-category.component';
import { TransitsComponent } from './components/transits/transits.component';
import { MainComponent } from './components/main/main.component';
import { SlideshowModule } from 'ng-simple-slideshow';
import { FeedbackCriteriaComponent } from './components/feedback-criteria/feedback-criteria.component';
import { AddUserComponent } from './components/add-user/add-user.component';
import { BusyHoursDiagramComponent } from './components/stops/components/busy-hours-diagram/busy-hours-diagram.component';
import { MessageComponent } from './components/message/message.component';
import { UserService } from './services/user.service';


import { StopsGridComponent } from './components/stops/stops-grid.component';
import { QuestionComponent } from './components/question/question.component';
import { AddQuestionComponent } from './components/question/add-question/add-question.component';
import { RaitingDiagramComponent } from './components/stops/components/raiting-diagram/raiting-diagram.component';
import { AverageRateComponent } from './components/stops/components/average-rate/average-rate.component';
import { DiagramService } from './services/diagram.service';
import { CallbackComponent } from './components/callback/callback.component';
import { OneQuestionComponent } from './components/question/one-question/one-question.component';
import { BusyStopsDiagramComponent } from './components/stops/components/busy-stops-diagram/busy-stops-diagram.component';
import { AgmCoreModule } from '@agm/core';
import { AgmDirectionModule } from 'agm-direction';
import { MapsComponent } from './components/maps/maps.component';


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
    AddUserComponent,
    MessageComponent,
    StopsGridComponent,
    QuestionComponent,
    AddQuestionComponent,
    RaitingDiagramComponent,
    AverageRateComponent,
    CallbackComponent,
    BusyHoursDiagramComponent,
    OneQuestionComponent,
    BusyStopsDiagramComponent,
    MapsComponent
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


  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatSidenavModule,
    HttpModule,
    HttpClientModule,
    MatNativeDateModule,
    AppRoutingModule,
    MatTableModule,
    MatTabsModule,
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
    MatGridListModule,
    MatIconModule,
    MatCheckboxModule,
    MatPaginatorModule,
    AgmDirectionModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }), AgmCoreModule.forRoot({ apiKey: 'AIzaSyBMbh1BuDtFteF5bxb03EKe2-hpKYre79g',}),
  ],
  providers: [ExcategoryService, UserService, DiagramService, AuthService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
