import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IProfile } from 'app/entities/profile/profile.model';
import { ProfileService } from 'app/entities/profile/service/profile.service';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { WalletService } from 'app/entities/wallet/service/wallet.service';
import { IAdmin } from '../admin.model';
import { AdminService } from '../service/admin.service';
import { AdminFormService } from './admin-form.service';

import { AdminUpdateComponent } from './admin-update.component';

describe('Admin Management Update Component', () => {
  let comp: AdminUpdateComponent;
  let fixture: ComponentFixture<AdminUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let adminFormService: AdminFormService;
  let adminService: AdminService;
  let profileService: ProfileService;
  let walletService: WalletService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AdminUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AdminUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AdminUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    adminFormService = TestBed.inject(AdminFormService);
    adminService = TestBed.inject(AdminService);
    profileService = TestBed.inject(ProfileService);
    walletService = TestBed.inject(WalletService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call relatedUser query and add missing value', () => {
      const admin: IAdmin = { id: 456 };
      const relatedUser: IProfile = { id: 5961 };
      admin.relatedUser = relatedUser;

      const relatedUserCollection: IProfile[] = [{ id: 656 }];
      jest.spyOn(profileService, 'query').mockReturnValue(of(new HttpResponse({ body: relatedUserCollection })));
      const expectedCollection: IProfile[] = [relatedUser, ...relatedUserCollection];
      jest.spyOn(profileService, 'addProfileToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ admin });
      comp.ngOnInit();

      expect(profileService.query).toHaveBeenCalled();
      expect(profileService.addProfileToCollectionIfMissing).toHaveBeenCalledWith(relatedUserCollection, relatedUser);
      expect(comp.relatedUsersCollection).toEqual(expectedCollection);
    });

    it('Should call systemWallet query and add missing value', () => {
      const admin: IAdmin = { id: 456 };
      const systemWallet: IWallet = { id: 17955 };
      admin.systemWallet = systemWallet;

      const systemWalletCollection: IWallet[] = [{ id: 15075 }];
      jest.spyOn(walletService, 'query').mockReturnValue(of(new HttpResponse({ body: systemWalletCollection })));
      const expectedCollection: IWallet[] = [systemWallet, ...systemWalletCollection];
      jest.spyOn(walletService, 'addWalletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ admin });
      comp.ngOnInit();

      expect(walletService.query).toHaveBeenCalled();
      expect(walletService.addWalletToCollectionIfMissing).toHaveBeenCalledWith(systemWalletCollection, systemWallet);
      expect(comp.systemWalletsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const admin: IAdmin = { id: 456 };
      const relatedUser: IProfile = { id: 9199 };
      admin.relatedUser = relatedUser;
      const systemWallet: IWallet = { id: 9121 };
      admin.systemWallet = systemWallet;

      activatedRoute.data = of({ admin });
      comp.ngOnInit();

      expect(comp.relatedUsersCollection).toContain(relatedUser);
      expect(comp.systemWalletsCollection).toContain(systemWallet);
      expect(comp.admin).toEqual(admin);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdmin>>();
      const admin = { id: 123 };
      jest.spyOn(adminFormService, 'getAdmin').mockReturnValue(admin);
      jest.spyOn(adminService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ admin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: admin }));
      saveSubject.complete();

      // THEN
      expect(adminFormService.getAdmin).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(adminService.update).toHaveBeenCalledWith(expect.objectContaining(admin));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdmin>>();
      const admin = { id: 123 };
      jest.spyOn(adminFormService, 'getAdmin').mockReturnValue({ id: null });
      jest.spyOn(adminService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ admin: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: admin }));
      saveSubject.complete();

      // THEN
      expect(adminFormService.getAdmin).toHaveBeenCalled();
      expect(adminService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdmin>>();
      const admin = { id: 123 };
      jest.spyOn(adminService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ admin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(adminService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProfile', () => {
      it('Should forward to profileService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(profileService, 'compareProfile');
        comp.compareProfile(entity, entity2);
        expect(profileService.compareProfile).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareWallet', () => {
      it('Should forward to walletService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(walletService, 'compareWallet');
        comp.compareWallet(entity, entity2);
        expect(walletService.compareWallet).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
