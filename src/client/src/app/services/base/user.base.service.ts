/**
 *
 *
  _____                      _              _ _ _     _   _     _        __ _ _
 |  __ \                    | |            | (_) |   | | | |   (_)      / _(_) |
 | |  | | ___    _ __   ___ | |_    ___  __| |_| |_  | |_| |__  _ ___  | |_ _| | ___
 | |  | |/ _ \  | '_ \ / _ \| __|  / _ \/ _` | | __| | __| '_ \| / __| |  _| | |/ _ \
 | |__| | (_) | | | | | (_) | |_  |  __/ (_| | | |_  | |_| | | | \__ \ | | | | |  __/
 |_____/ \___/  |_| |_|\___/ \__|  \___|\__,_|_|\__|  \__|_| |_|_|___/ |_| |_|_|\___|

 * DO NOT EDIT THIS FILE!!
 *
 *  FOR CUSTOMIZE userBaseService PLEASE EDIT ../user.service.ts
 *
 *  -- THIS FILE WILL BE OVERWRITTEN ON THE NEXT SKAFFOLDER'S CODE GENERATION --
 *
 */
 // DEPENDENCIES
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AngularFirestore, AngularFirestoreDocument, AngularFirestoreCollection, DocumentReference } from '@angular/fire/firestore';
import { AngularFireFunctions } from '@angular/fire/functions';

// CONFIG
import { environment } from '../../../environments/environment';

// MODEL
import { User } from '../../domain/hulkstoreweb_db/user';

/**
 * THIS SERVICE MAKE HTTP REQUEST TO SERVER, FOR CUSTOMIZE IT EDIT ../User.service.ts
 */

/*
 * SCHEMA DB User
 *
	{
		mail: {
			type: 'String'
		},
		name: {
			type: 'String'
		},
		password: {
			type: 'String',
			required : true
		},
		roles: {
			type: 'String'
		},
		surname: {
			type: 'String'
		},
		username: {
			type: 'String',
			required : true
		},
		//RELATIONS
		//EXTERNAL RELATIONS
	}
 *
 */
@Injectable()
export class UserBaseService {

    private userCollection: AngularFirestoreCollection<User>;
    constructor(
        private afs: AngularFirestore,
        private fns: AngularFireFunctions
    ) {
        this.userCollection = afs.collection<User>('user');
    }


    // CRUD METHODS

    /**
    * UserService.create
    *   @description CRUD ACTION create
    *
    */
    create(item: User): Promise<DocumentReference> {
        return this.userCollection.add(item);
    }

    /**
    * UserService.delete
    *   @description CRUD ACTION delete
    *   @param ObjectId id Id
    *
    */
    remove(id: string) {
        const itemDoc: AngularFirestoreDocument<any> = this.userCollection.doc(id);
        itemDoc.delete();
    }

    /**
    * UserService.get
    *   @description CRUD ACTION get
    *   @param ObjectId id Id 
    *
    */
    get(id: string): AngularFirestoreDocument<User> {
        return this.afs.doc<User>('user/' + id);
    }

    /**
    * UserService.list
    *   @description CRUD ACTION list
    *
    */
    list(): Observable<User[]> {
        return this.afs.collection('user').snapshotChanges().pipe(
            map(actions => actions.map(a => {
                const data = a.payload.doc.data() as User;
                const id = a.payload.doc.id;
                return { id, ...data };
            }))
        );
    }

    /**
    * UserService.update
    *   @description CRUD ACTION update
    *   @param ObjectId id Id
    *
    */
    update(itemDoc: AngularFirestoreDocument<User>, item: User): Promise<void> {
        return itemDoc.update(item);
    }


    // Custom APIs


    /**
    * UserService.changePassword
    *   @description Change password of user from admin
    *   @returns object
    *
    */
    changePassword(...params: any[]): Observable<any> {
        const callable = this.fns.httpsCallable('change-password');
        return callable(params);
    }

}
