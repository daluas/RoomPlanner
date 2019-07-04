import { Injectable } from '@angular/core';
import { LoginModel } from '../../models/LoginModel';
import { LoggedUser } from '../../models/LoggedUser';
import { HttpClient, HttpHeaders, HttpResponse, HttpErrorResponse, HttpParams } from "@angular/common/http";

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
			console.log("token expired");
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
		localStorage.setItem("user-data", JSON.stringify(this.currentUser));
		this.currentUserSubscriber.next(this.currentUser);
	}

	refreshToken(): Promise<Object> {
		let params = new HttpParams();
		params.set("grant_type", "refresh_token")
		params.set('refresh_token', this.token.refresh_token);
		console.log(params);
		return this.httpClient.post(`${this.backendUrl}/oauth/token`, params).toPromise();
	}

	authenticateUser(loginModel: LoginModel) : Promise<Object> {
		let params = new HttpParams();
		params = params.set("grant_type", "password");
		params = params.set('username', loginModel.email);
		params = params.set('password', loginModel.password);
	
	
		return this.httpClient.post(`${this.backendUrl}/oauth/token`, params).toPromise()
			.then(token => {
				console.log(token)
				let params = new HttpParams()
				params = params.append("email", loginModel.email)
				// x = 
				return this.httpClient.get(`${this.backendUrl}/api/users`, { params: params }).toPromise()
			})
	}


	getUser(userModel: LoginModel) {
		let params = new HttpParams()
		params = params.append("email", userModel.email)
		return this.httpClient.get(`${this.backendUrl}/api/users`, { params: params }).toPromise()
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
