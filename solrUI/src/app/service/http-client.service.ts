import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';


export class Movie{
  constructor(
    public id:String,
    public name:String,
    public date:String,
    public rate:String,
    public genre:Array<String>,
  ) {}
}

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

  constructor(
    private httpClient:HttpClient
  ) { }

  getTitles(search:String, start:any)
  {
    let params = new HttpParams();

    // Begin assigning parameters
    params = params.append('title', "*"+search+"*");
    params = params.append('start', start);
    console.log("test call");
    return this.httpClient.get<Movie[]>('http://localhost:8080/titles',{ params: params });
  }
}
