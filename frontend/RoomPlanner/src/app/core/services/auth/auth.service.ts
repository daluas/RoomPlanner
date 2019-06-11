import { Injectable } from '@angular/core';
import { LoginModel } from '../../models/LoginUser';
import { LoggedUser } from '../../models/LoggedUser';
import { HttpClient, HttpHeaders, HttpResponse, HttpErrorResponse } from "@angular/common/http";
import { LoginToken } from '../../models/LoginToken';
import { Observable, of, Subject, Subscriber } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
	providedIn: 'root'
})
export class AuthService {



	private backendUrl: string = '';
	private userData;
	private currentUser: LoggedUser;
	private currentUserSubject: Subscriber<LoggedUser> = new Subscriber<LoggedUser>();

	constructor(
		private httpClient: HttpClient,
		private router: Router
	) {
		this.checkIfLoggedIn()
	}

	checkIfLoggedIn(): void {
		if (localStorage.getItem('access-token') == null) {
			return;
		}

		let userParsed = JSON.parse(localStorage.getItem('user-data'))
		if (userParsed) {
			let user: LoggedUser = new LoggedUser().create(userParsed);
			console.log(user);
			this.setCurrentUser(user);
		}

	}

	setCurrentUser(loggedUserModel: LoggedUser): void {
		this.currentUser = loggedUserModel;
		console.log(this.currentUser);
		this.currentUserSubject.next(this.currentUser);
	}

	authenticateUser(credentials: LoginModel) {
		return this.httpClient.post(`${this.backendUrl}/auth/signin`, credentials)
			.toPromise()
		// .then((data) => {
		// 	console.log(data);

		// 	let user: LoggedUser = new LoggedUser().create(data);
		// 	this.setCurrentUser(user);

		// 	return of(new HttpResponse({
		// 		status: 200,
		// 		body: user
		// 	}));

		// }).catch(error => {
		// 	console.log(error)
		// 	return error
		// })
	}

	checkRoomPassword(password: string): Promise<boolean> {
		return of(true).toPromise();
	}

	getCurrentUser(): Observable<LoggedUser> {
		return new Observable((observer) => {
			observer.next(this.currentUser);
			this.currentUserSubject = observer;
		});
	}

	logout() {
		localStorage.clear();
		this.currentUser = null;
		this.currentUserSubject.next(this.currentUser);
		this.router.navigate(['/login']);
	}


}
