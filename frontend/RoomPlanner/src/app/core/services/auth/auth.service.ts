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
		if (localStorage.getItem('user-data') == null) return;

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

	authenticateUser(credentials: LoginModel): Promise<Object> {
		return this.httpClient.post(`${this.backendUrl}/auth/signin`, credentials)
			.toPromise()
	}

	async checkRoomPassword(password: string): Promise<Object> {
		return new Promise(async (resolve) => {
			let userParsed = JSON.parse(localStorage.getItem('user-data'))
			if (userParsed) {
				let user: LoggedUser = new LoggedUser().create(userParsed);
				let loginModel: LoginModel = new LoginModel().create({ email: user.email, password: password });
				console.log(user, loginModel);
				await this.authenticateUser(loginModel)
					.then((data) => {
						console.log(data);
						if (data) {
							resolve(true);
						}
						resolve(false);
					})
					.catch((err)=>{
						console.log(err);
						resolve(false);
					})
			}
			resolve(false);
		})
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

	logout() {
		localStorage.clear();
		this.currentUser = null;
		this.currentUserSubject.next(this.currentUser);
		this.router.navigate(['/login']);
	}


}
