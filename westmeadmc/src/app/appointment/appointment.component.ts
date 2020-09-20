import { Component, OnInit, ViewChildren, QueryList, ViewEncapsulation } from '@angular/core';
import { AppointmentDTO } from '../models/AppointmentDTO';
import { UserDTO } from '../models/UserDTO';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../user.service';
import { TimeDTO } from '../models/TimeDTO';
import { session_user_key, session_user_type } from '../Constants';
import {MatDialog} from '@angular/material/dialog';
import { BookingTermsAndConditonsDialogComponent } from '../booking-terms-and-conditons-dialog/booking-terms-and-conditons-dialog.component';
import { Router } from "@angular/router";

@Component({
  selector: 'app-appointment',
  templateUrl: './appointment.component.html',
  styleUrls: ['./appointment.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AppointmentComponent implements OnInit {

  selectedDoctor:UserDTO;
  selectedDate: Date;
  selectedTime: TimeDTO;
  reason: string;
  agreeCB: boolean;
  isAdmin: boolean;
  patientId: string;
  doctors:MatTableDataSource<UserDTO>;
  unModifiableDoctors:UserDTO[]=[];
  doctorColumns: string[] = ['firstName', 'lastName', 'age', 'gender', 'qualification', 'experience'];

  appointmentHistoryColumns: string[] = ['index', 'dateTime', 'doctorName', 'status', 'billDetails'];
  appointments:MatTableDataSource<AppointmentDTO>;

  @ViewChildren(MatPaginator) paginators: QueryList<MatPaginator>;
  times:TimeDTO[]=[];
  constructor(private userService: UserService, private snackBar: MatSnackBar, public dialog: MatDialog,private router: Router) { }

  ngOnInit(): void {
    
    var appointment:AppointmentDTO = new AppointmentDTO();
    appointment.billDetails = "$50.00"
    appointment.dateTime = "2020-09-05 2:30 pm";
    appointment.doctorName = "Dr. Mannivanan MBBS";
    appointment.status = "Completed";

    this.appointments =  new MatTableDataSource<AppointmentDTO>();
    this.appointments.data.push(appointment);

    this.selectedDoctor = new UserDTO();
    this.selectedDate = new Date();
    this.selectedTime = new TimeDTO();
    this.doctors = new MatTableDataSource<UserDTO>();
    this.isAdmin = (sessionStorage.getItem(session_user_type) == "admin");
    this.userService.getApprovedDoctors().subscribe(resp=>{
      this.unModifiableDoctors = resp;
      var date:string = this.getDateAsString(this.selectedDate);
      var doctors:UserDTO[] = [];
      this.unModifiableDoctors.forEach(doctorEle=>{
        if(doctorEle.availableTimes[date]){
          doctors.push(doctorEle);
        }
      });
      this.doctors.data = doctors;
    })
    
  }

  ngAfterViewInit() {
    this.doctors.paginator = this.paginators.first;
    this.appointments.paginator = this.paginators.last;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.appointments.filter = filterValue.trim().toLowerCase();
  }

  doctorApplyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.doctors.filter = filterValue.trim().toLowerCase();
  }

  onSelectDoctor(doctor:UserDTO){
    this.selectedDoctor = doctor;   
    var date: string = this.getDateAsString(new Date());
    if(this.selectedDate){
      date = this.getDateAsString(this.selectedDate);
    }
    this.times = doctor.availableTimes[date];
    this.doctors.data.forEach(doctorEle => {
      if(doctorEle.emailId == doctor.emailId){
        doctorEle.highlighted = true
      } else {
        doctorEle.highlighted = false;
      }
    });

    
  }
  onSelectTime(time:TimeDTO, isSelected:boolean){
   this.times.forEach(timeEle => {
      timeEle.isSelected = false;
    });

    time.isSelected = isSelected;
    this.selectedTime = time;
    console.log(time.time.slice(0, 5));
  }

  public getDateAsString(date: Date):string{
    var month = "0", day = "0";
    if((date.getMonth() + 1) < 10){
      month += (date.getMonth()+1);
    } else {
      month = (date.getMonth() + 1).toString();
    }

    if((date.getDate()) < 10){
      day += (date.getDate());
    } else {
      day = (date.getDate()).toString();
    }
    return date.getFullYear() + "-" + month + "-" + day;
  }

  onChangeDate(){
    /**get the available time for this date for selected doctor*/
    console.log(this.selectedDate);
    this.times = this.selectedDoctor.availableTimes[this.getDateAsString(this.selectedDate)];

    /**Filter the doctors who are available for the selected date*/
    var date:string = this.getDateAsString(this.selectedDate);
    var doctors:UserDTO[] = [];
    this.unModifiableDoctors.forEach(doctorEle=>{
      if(doctorEle.availableTimes[date]){
        doctors.push(doctorEle);
      }
    });
    this.doctors.data = doctors;
    
    /**clean the selected time when date changes */
    this.selectedTime = new TimeDTO();
  }

  bookAppointment(){
    if(this.validateBookAppointment()){
      var patient:UserDTO = JSON.parse(sessionStorage.getItem(session_user_key));
      var patientEmailId;
      if(this.isAdmin){
        patientEmailId = this.patientId;
      } else {
        patientEmailId = patient.emailId;
      }
      this.userService.bookAppointment(patientEmailId, this.selectedDoctor.emailId, this.getDateAsString(this.selectedDate), this.selectedTime.time, this.reason).subscribe(resp=>{
        if(resp){
          this.showSnackBar("Appointment Booked Successfully");
          this.router.navigate(["/"]);
        }
      }, err=>{
        var error:string = err.error;
        this.showSnackBar(error.substring(error.indexOf(":") + 1));
      });
    }
  }

  private validateBookAppointment(): boolean{
    if(!this.selectedDoctor.emailId){
      this.showSnackBar("Please select a doctor to book appointment");
      return false;
    } else if(!this.selectedTime.time){
      this.showSnackBar("Please select a time to book appointment");
      return false;
    } else if(!this.reason){
      this.showSnackBar("Please tell us the purpose of visit in step 2");
      return false;
    } else if(!this.agreeCB){
      this.showSnackBar("Please agree the terms and conditons");
      return false;
    } else if(this.isAdmin && !this.patientId){
      this.showSnackBar("Please enter patient Id");
      return false;
    }

    return true;
  }

  private showSnackBar(msg1: string, msg2?: string){
    this.snackBar.open(msg1, msg2, {
      duration: 1000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
  }

  openTermsAndConditions(){
    const dialogRef = this.dialog.open(BookingTermsAndConditonsDialogComponent, {
      width: '500px'
    });
  }
}
