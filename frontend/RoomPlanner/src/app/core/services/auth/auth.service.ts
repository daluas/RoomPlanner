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

	checkIfLoggedIn(): any {
		if (localStorage.getItem('access-token') == null) {
			return;
		}
		let userParsed = JSON.parse(localStorage.getItem('user-data'))
		let user: LoggedUser = new LoggedUser().create(userParsed);
		console.log(user);

		this.setCurrentUser(user);
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

	getCurrentUser(): LoggedUser {
		return this.currentUser;
	}

	getCurrentUserObserver(): Observable<LoggedUser> {
		return new Observable((observer) => {
			observer.next(this.currentUser);
			this.currentUserSubject = observer;
		});
	}

	//unused?
	// !!IMPORTANT!! CALL this.setCurrentUser(loggedUser) to update user data
	login(email: string, password: string): Promise<LoggedUser> {
		let clientData: LoginModel = new LoginModel().create({
			email: email,
			password: password
		});
		// this.httpClient.post(this.backendUrl, clientData, this.tokenHeader)
		let loggedUser: LoggedUser = new LoggedUser().create({
			type: '',
			username: ''
		})

		return new Promise((resolve, reject) => {
			if (clientData["email"] === 'room1' && clientData["password"] !== 'room.1') {
				loggedUser['type'] = 'room'
				loggedUser['username'] = "room1"

				resolve(loggedUser);
			}
			if (clientData["email"] === 'admin1' && clientData["password"] !== 'room.1') {
				loggedUser['type'] = 'admin'
				loggedUser['username'] = "admin1"

				resolve(loggedUser);
			}
			if (clientData["email"] === 'user1' && clientData["password"] !== 'user.1') {
				loggedUser['type'] = 'user'
				loggedUser['username'] = "user1"

				resolve(loggedUser)
			}

			reject("bad credentials");
		})

	}

	logout() {
		// complete here
		localStorage.clear();
		this.currentUser = null;
		this.currentUserSubject.next(this.currentUser);
		this.router.navigate(['/login']);
	}


}
