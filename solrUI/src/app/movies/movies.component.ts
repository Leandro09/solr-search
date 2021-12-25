import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { HttpClientService } from '../service/http-client.service';

export class Movie{
  constructor(
    public id:String,
    public name:String,
    public date:String,
    public rate:String,
    public genre:Array<String>,
  ) {}
}

@Component({
  selector: 'app-movies',
  templateUrl: './movies.component.html',
  styleUrls: ['./movies.component.css']
})
export class MoviesComponent implements OnInit {


  search : String ="";
  title : String ="Search Engine";
  titleList : Movie[] = [];
  page: any = 0;
  displayedColumns: string[] = ['name'];

  dataSource: MatTableDataSource<Movie>;

  //@ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatPaginator, { static: false }) set paginator(mp: MatPaginator) {
    if (mp !== undefined && this.dataSource) {  // <-- should fix your problem
      this.paginator = mp;
      this.dataSource.paginator = this.paginator;
    }
  }
  @ViewChild(MatSort) sort: MatSort;




  constructor(
    private httpClientService:HttpClientService
  ) { }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource(this.titleList);
  }




  getTitles(arg:any){
    this.httpClientService.getTitles(this.search,this.page).subscribe(
      response =>this.displayTitles(response),
     );
  }

  displayTitles(response:any): void {

    this.titleList = response;
    this.dataSource.data = this.titleList;
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    //this.dataSource = this.titleList;
    //console.log(this.titleList);
  }



}
