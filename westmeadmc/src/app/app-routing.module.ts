import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { AppointmentComponent } from './appointment/appointment.component';
import { TreatmentHistoryComponent } from './treatment-history/treatment-history.component';
import { ManageAccountComponent } from './manage-account/manage-account.component';
import { HealthTipsComponent } from './health-tips/health-tips.component';
import { AdminApprovalComponent } from './admin-approval/admin-approval.component';
import { DoctorAvailabilityComponent } from './doctor-availability/doctor-availability.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'about-us', component: AboutUsComponent },
  { path: 'health-tips', component: HealthTipsComponent },
  { path: 'appointment', component: AppointmentComponent },
  { path: 'treatmentHistory', component: TreatmentHistoryComponent },
  { path: 'manage-account', component: ManageAccountComponent },
  { path: 'adminApprovals', component: AdminApprovalComponent },
  { path: 'doctorAvailability', component: DoctorAvailabilityComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
