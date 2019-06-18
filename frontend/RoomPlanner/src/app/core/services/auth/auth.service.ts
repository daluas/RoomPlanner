import { Injectable } from '@angular/core';
import { LoginModel } from '../../models/LoginModel';
import { LoggedUser } from '../../models/LoggedUser';
import { HttpClient, HttpHeaders, HttpResponse, HttpErrorResponse } from "@angular/common/http";

import { LoginToken } from '../../models/LoginToken';
import { Observable, of, Subject, Subscriber } from 'rxjs';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material';

@Injectable({
	providedIn: 'root'
})
export class AuthService {

	private token: LoginToken;
	private backendUrl: string = 'http://178.22.68.114:8081';
	private currentUser: LoggedUser;
	private currentUserSubscriber: Subscriber<LoggedUser> = new Subscriber<LoggedUser>();

	constructor(
		public httpClient: HttpClient,
		public router: Router,
		private _snackBar: MatSnackBar
	) {
		this.setInitialUserFromLocalStorage()
	}

	setInitialUserFromLocalStorage(): void {
		if (localStorage.getItem('user-data') == null) {
			return;
		}
		if (localStorage.getItem('access-token') == null) {
			return;
		}

		this.token = new LoginToken().create(JSON.parse(localStorage.getItem("access-token")))
		let userParsed = JSON.parse(localStorage.getItem('user-data'))
		let user: LoggedUser = new LoggedUser().create(userParsed);

		if (this.token.isExpired) {
			this.refreshToken()
				.then(data => {
					console.log(data);
				})
				.catch(error => {
					console.log(error)
				});
		}
		this.OnCurrentUserChanged(user);

	}

	OnCurrentUserChanged(loggedUserModel: LoggedUser): void {
		this.currentUser = loggedUserModel;
		this.currentUserSubscriber.next(this.currentUser);
	}

	refreshToken(): Promise<Object> {
		return this.httpClient.post(`${this.backendUrl}/oauth/token`, {
			grant_type: "refresh_token",
			refresh_token: this.token.refresh_token
		}).toPromise();
	}

	authenticateUser(loginModel: LoginModel): Promise<Object> {
		return this.httpClient.post(`${this.backendUrl}/oauth/token`, {
			grant_type: "password",
			username: loginModel.email,
			password: loginModel.password
		}).toPromise()
			// .then(data => {
			// 	console.log(data)
			// 	return new Promise((res, rej) => {
			// 		res(true);
			// 	})
			// })
			// .catch(error => {
			// 	//this should go on .then
			// 	return new Promise((res, rej) => {
			// 		res(error);
			// 	})
			// })
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
					.catch((err) => {
						console.log(err);
						resolve(false);
					})
			}
			resolve(false);
		})
	}

	getCurrentUser(): LoggedUser {
		console.log(this.currentUser);
		return this.currentUser;
	}

	getCurrentUserObserver(): Observable<LoggedUser> {
		return new Observable((observer) => {
			observer.next(this.currentUser);
			this.currentUserSubscriber = observer;
		});
	}

	logout() {
		localStorage.clear();
		this.currentUser = null;
		this.currentUserSubscriber.next(this.currentUser);
		this.router.navigate(['/login']);
	}


}
