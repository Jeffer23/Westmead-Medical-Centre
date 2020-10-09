import { Component, OnInit } from '@angular/core';
import { session_isUserLoggedIn } from '../Constants';
import {MatDialog} from '@angular/material/dialog';
import { LoginComponent } from '../login/login.component';
import { Router } from '@angular/router';
import { session_user_type } from '../Constants';

@Component({
  selector: 'app-center-pane',
  templateUrl: './center-pane.component.html',
  styleUrls: ['./center-pane.component.css']
})
export class CenterPaneComponent implements OnInit {

    isUserLoggedIn:string;
    isDoctor : boolean;
  constructor(public dialog: MatDialog, private router: Router) { }

  ngOnInit(): void {
    this.isUserLoggedIn = JSON.parse(sessionStorage.getItem(session_isUserLoggedIn));
    this.isDoctor = (sessionStorage.getItem(session_user_type) == "doctor");
  }
  imageObject = [ {
    image: 'http://localhost:8080/assets/images/banners/banner1.png',
    thumbImage : 'http://localhost:8080/assets/images/banners/banner1.png'
}, {
    image: 'http://localhost:8080/assets/images/banners/banner2.png',
    thumbImage : 'http://localhost:8080/assets/images/banners/banner2.png'
}, {
    image: 'http://localhost:8080/assets/images/banners/banner3.png',
    thumbImage : 'http://localhost:8080/assets/images/banners/banner3.png'
},{
    image: 'http://localhost:8080/assets/images/banners/banner4.png',
    thumbImage : 'http://localhost:8080/assets/images/banners/banner4.png'
}];


    bookAppointment(){
        if(this.isUserLoggedIn){
            this.router.navigate(['/', 'appointment']);
        } else {
            this.openLoginDialog();
        }
    }

    viewAppointments(){
        if(this.isUserLoggedIn){
            this.router.navigate(['/', 'view-appointment']);
        } else {
            this.openLoginDialog();
        }
    }

    openLoginDialog():void{
        const dialogRef = this.dialog.open(LoginComponent, {
          width: '300px'
        });
      }

}
