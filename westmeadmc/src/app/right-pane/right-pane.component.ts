import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-right-pane',
  templateUrl: './right-pane.component.html',
  styleUrls: ['./right-pane.component.css']
})
export class RightPaneComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
   
  }

  ngAfterViewInit() {
    var container = document.getElementById('contactDiv');
    var rowToScrollTo = document.getElementById('map');

    container.scrollTop = rowToScrollTo.offsetTop;
  }

}
