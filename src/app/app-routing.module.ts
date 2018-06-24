import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './services/auth/auth.guard';

import { NonExCategoryComponent } from './components/non-ex-category/non-ex-category.component';
import { TransitsComponent } from './components/transits/transits.component';
import { MainComponent } from './components/main/main.component';
import { FeedbackCriteriaComponent } from './components/feedback-criteria/feedback-criteria.component';
import { AddUserComponent } from './components/add-user/add-user.component';
import {StopsGridComponent} from './components/stops/stops-grid.component';
import { QuestionComponent} from './components/question/question.component';
import { AddQuestionComponent } from './components/question/add-question/add-question.component';
import { OneQuestionComponent } from './components/question/one-question/one-question.component';
import {MapsComponent} from './components/maps/maps.component';
const routes: Routes = [
  {
    path: 'category/:top/:city', component: NonExCategoryComponent
    // children: [
    //   {
    //     path: ':id', component: TransitsComponent
    //   }
    // ]
  },
  {path: 'category/:top/:city/:id', component: TransitsComponent},


  {
    path: 'main', component: MainComponent
  },
  {
    path: 'feedback-criteria', component: FeedbackCriteriaComponent
  },

  {
    path: 'question', component: QuestionComponent},
  {path: 'question/add-question', component: AddQuestionComponent},

  {path: 'question/:id', component: OneQuestionComponent},

  {
    path: 'user/add', component: AddUserComponent
  },
  {path: 'show-transit-scheme/:id', component: StopsGridComponent},
  {path: 'direction/:id', component: MapsComponent}



];

@NgModule({
  imports: [
    [RouterModule.forRoot(routes)]
  ],
  providers: [AuthGuard],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
