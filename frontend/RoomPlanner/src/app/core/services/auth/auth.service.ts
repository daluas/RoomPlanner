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
	// private backendUrl: string = 'localhost:8081';
	private backendUrl: string = '';
	private currentUser: LoggedUser;
	private currentUserSubject: Subscriber<LoggedUser> = new Subscriber<LoggedUser>();

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
		let expirationDate: Date = new Date(this.token.expirationDate)
		let now = new Date(Date.now());
		if (expirationDate.getTime() < now.getTime()) {
			this._snackBar.open(
				`Token is expired (auth)`,
				'Close',
				{
					duration: 5000
				}
			);
			console.log("token is old:")
			console.log("using refresh token: ", this.token);

			this.refreshToken(this.token)
				.then((token: LoginToken) => {
					let newExpDate = new Date(token.expirationDate);
					this._snackBar.open(
						'Refresh finished',
						'Close',
						{
							duration: 5000
						}
					);
					setTimeout(()=>{
						console.log("REFRESHING TOKEN")
						this.refreshToken(token)
					}, (newExpDate.getTime() - now.getTime()))
					// setInterval(()=>{
					// 	console.log("REFRESHING TOKEN")
					// 	console.log(this);
					// 	this.refreshToken(token)
					// }, 20000)
				})
				.catch(error=>{
					this._snackBar.open(
						`refresh error: ${error}`,
						'Close',
						{
							duration: 7000
						}
					);
					this.logout();
				})
		}
		else{
			this._snackBar.open(
				`Token is alive (auth)`,
				'Close',
				{
					duration: 2000
				}
			);

			setTimeout(()=>{
				this.refreshToken(new LoginToken().create({
					value: "",
					expirationDate: (Date.now()),
					refresh_token: "REFRESH"
				}))
			}, (expirationDate.getTime() - now.getTime()))// * 1000)
		}
		this.OnCurrentUserChanged(user);

	}

	OnCurrentUserChanged(loggedUserModel: LoggedUser): void {
		this.currentUser = loggedUserModel;
		this.currentUserSubject.next(this.currentUser);
	}

	refreshToken(token: LoginToken): Promise<Object> {
		return this.httpClient.post(`${this.backendUrl}/auth/refresh`, {refresh_token: "REFRESH"}).toPromise();
	}

	authenticateUser(credentials: LoginModel): Promise<Object> {
		return this.httpClient.post(`${this.backendUrl}/auth/signin`, credentials).toPromise()
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
