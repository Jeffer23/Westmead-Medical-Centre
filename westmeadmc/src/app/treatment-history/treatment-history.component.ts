import { Component, OnInit, ViewChild, AfterViewInit, ViewEncapsulation  } from '@angular/core';
import { TreatmentDTO } from '../models/TreatmentDTO';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-treatment-history',
  templateUrl: './treatment-history.component.html',
  styleUrls: ['./treatment-history.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class TreatmentHistoryComponent implements OnInit {

  displayedColumns: string[] = ['index', 'dateTime', 'doctorName', 'comment', 'documentLink'];
  treatments = new MatTableDataSource<TreatmentDTO>();
  @ViewChild(MatPaginator) paginator: MatPaginator;
  constructor() { }

  ngAfterViewInit() {
    this.treatments.paginator = this.paginator;
  }
  ngOnInit(): void {
    var treatment:TreatmentDTO = new TreatmentDTO();
    treatment.comment = "Sleep well";
    treatment.dateTime = "2020-09-05 2:30 pm";
    treatment.doctorName = "Dr. Manivannan MBBS";
    treatment.documentLink = "https://";

    this.treatments.data.push(treatment);
    var treatment:TreatmentDTO = new TreatmentDTO();
    treatment.comment = "Sleep well";
    treatment.dateTime = "2020-09-05 2:30 pm";
    treatment.doctorName = "Dr. Manivannan MBBS";
    treatment.documentLink = "https://";

    this.treatments.data.push(treatment);
    var treatment:TreatmentDTO = new TreatmentDTO();
    treatment.comment = "Sleep well";
    treatment.dateTime = "2020-09-05 2:30 pm";
    treatment.doctorName = "Dr. Manivannan MBBS";
    treatment.documentLink = "https://";

    this.treatments.data.push(treatment);
    var treatment:TreatmentDTO = new TreatmentDTO();
    treatment.comment = "Sleep well";
    treatment.dateTime = "2020-09-05 2:30 pm";
    treatment.doctorName = "Dr. Manivannan MBBS";
    treatment.documentLink = "https://";

    this.treatments.data.push(treatment);
    var treatment:TreatmentDTO = new TreatmentDTO();
    treatment.comment = "Sleep well";
    treatment.dateTime = "2020-09-05 2:30 pm";
    treatment.doctorName = "Dr. Manivannan MBBS";
    treatment.documentLink = "https://";

    this.treatments.data.push(treatment);
    var treatment:TreatmentDTO = new TreatmentDTO();
    treatment.comment = "Sleep well";
    treatment.dateTime = "2020-09-05 2:30 pm";
    treatment.doctorName = "Dr. Manivannan MBBS";
    treatment.documentLink = "https://";

    this.treatments.data.push(treatment);
    var treatment:TreatmentDTO = new TreatmentDTO();
    treatment.comment = "Sleep well";
    treatment.dateTime = "2020-09-05 2:30 pm";
    treatment.doctorName = "Dr. Manivannan MBBS";
    treatment.documentLink = "https://";

    this.treatments.data.push(treatment);
    var treatment:TreatmentDTO = new TreatmentDTO();
    treatment.comment = "Sleep well";
    treatment.dateTime = "2020-09-05 2:30 pm";
    treatment.doctorName = "Dr. Manivannan MBBS";
    treatment.documentLink = "https://";

    this.treatments.data.push(treatment);
    var treatment:TreatmentDTO = new TreatmentDTO();
    treatment.comment = "Sleep well";
    treatment.dateTime = "2020-09-06 2:30 pm";
    treatment.doctorName = "Dr. Priya MBBS";
    treatment.documentLink = "https://";

    this.treatments.data.push(treatment);

  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.treatments.filter = filterValue.trim().toLowerCase();
  }

}
