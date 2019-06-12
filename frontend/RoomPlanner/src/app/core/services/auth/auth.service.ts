import { Injectable } from '@angular/core';
import { LoginModel } from '../../models/LoginModel';
import { LoggedUser } from '../../models/LoggedUser';
import { HttpClient, HttpHeaders, HttpResponse, HttpErrorResponse } from "@angular/common/http";

import { LoginToken } from '../../models/LoginToken';
import { Observable, of, Subject, Subscriber } from 'rxjs';
import { Router } from '@angular/router';

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
		public router: Router
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
		let token = new LoginToken().create(JSON.parse(localStorage.getItem("access-token")))
		console.log(token)

		let userParsed = JSON.parse(localStorage.getItem('user-data'))
		let user: LoggedUser = new LoggedUser().create(userParsed);
		console.log(user);


		let expirationDate: Date = new Date(token.expirationDate)
		let now = new Date(Date.now());

		if (expirationDate < now) {
			// alert('Token is expired');
			this.refreshToken(token.refresh_token)
				.then(data => {
					console.log("Refresh finished")
					console.log(data);
				})
				.catch(error=>{
					console.log("refresh error");
					console.log(error);
				})

			// token.expirationDate = new Date(new Date(Date.now()).getTime() + 60 * 2 * 1000)
			// //call to backend to retrieve updated token  ||  go to login again

			// localStorage.setItem('access-token', JSON.stringify(token));
			// setTimeout(this.refreshToken, 60 * 2 * 1000)
		}
		else{
			setTimeout(()=>{
				console.log(token.refresh_token);
				this.refreshToken(token.refresh_token)
			}, (expirationDate.getTime() - now.getTime()))// * 1000)
		}



		// this.OnCurrentUserChanged(user);
		this.token = token;
		this.OnCurrentUserChanged(user);

	}

	OnCurrentUserChanged(loggedUserModel: LoggedUser): void {
		this.currentUser = loggedUserModel;
		this.currentUserSubject.next(this.currentUser);

	}

	refreshToken(refresh_token: string): Promise<Object> {
		return this.httpClient.post(`${this.backendUrl}/auth/refresh`, {refresh_token: refresh_token}).toPromise();
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
