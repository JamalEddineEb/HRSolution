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
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { IDomain } from 'app/entities/domain/domain.model';
import { DomainService } from 'app/entities/domain/service/domain.service';
import { IRecruiter } from '../recruiter.model';
import { RecruiterService } from '../service/recruiter.service';
import { RecruiterFormService } from './recruiter-form.service';

import { RecruiterUpdateComponent } from './recruiter-update.component';

describe('Recruiter Management Update Component', () => {
  let comp: RecruiterUpdateComponent;
  let fixture: ComponentFixture<RecruiterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recruiterFormService: RecruiterFormService;
  let recruiterService: RecruiterService;
  let profileService: ProfileService;
  let walletService: WalletService;
  let applicationService: ApplicationService;
  let domainService: DomainService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RecruiterUpdateComponent],
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
      .overrideTemplate(RecruiterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecruiterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recruiterFormService = TestBed.inject(RecruiterFormService);
    recruiterService = TestBed.inject(RecruiterService);
    profileService = TestBed.inject(ProfileService);
    walletService = TestBed.inject(WalletService);
    applicationService = TestBed.inject(ApplicationService);
    domainService = TestBed.inject(DomainService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call relatedUser query and add missing value', () => {
      const recruiter: IRecruiter = { id: 456 };
      const relatedUser: IProfile = { id: 18574 };
      recruiter.relatedUser = relatedUser;

      const relatedUserCollection: IProfile[] = [{ id: 21596 }];
      jest.spyOn(profileService, 'query').mockReturnValue(of(new HttpResponse({ body: relatedUserCollection })));
      const expectedCollection: IProfile[] = [relatedUser, ...relatedUserCollection];
      jest.spyOn(profileService, 'addProfileToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recruiter });
      comp.ngOnInit();

      expect(profileService.query).toHaveBeenCalled();
      expect(profileService.addProfileToCollectionIfMissing).toHaveBeenCalledWith(relatedUserCollection, relatedUser);
      expect(comp.relatedUsersCollection).toEqual(expectedCollection);
    });

    it('Should call wallet query and add missing value', () => {
      const recruiter: IRecruiter = { id: 456 };
      const wallet: IWallet = { id: 31290 };
      recruiter.wallet = wallet;

      const walletCollection: IWallet[] = [{ id: 11393 }];
      jest.spyOn(walletService, 'query').mockReturnValue(of(new HttpResponse({ body: walletCollection })));
      const expectedCollection: IWallet[] = [wallet, ...walletCollection];
      jest.spyOn(walletService, 'addWalletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recruiter });
      comp.ngOnInit();

      expect(walletService.query).toHaveBeenCalled();
      expect(walletService.addWalletToCollectionIfMissing).toHaveBeenCalledWith(walletCollection, wallet);
      expect(comp.walletsCollection).toEqual(expectedCollection);
    });

    it('Should call Application query and add missing value', () => {
      const recruiter: IRecruiter = { id: 456 };
      const applications: IApplication[] = [{ id: 27827 }];
      recruiter.applications = applications;

      const applicationCollection: IApplication[] = [{ id: 22575 }];
      jest.spyOn(applicationService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationCollection })));
      const additionalApplications = [...applications];
      const expectedCollection: IApplication[] = [...additionalApplications, ...applicationCollection];
      jest.spyOn(applicationService, 'addApplicationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recruiter });
      comp.ngOnInit();

      expect(applicationService.query).toHaveBeenCalled();
      expect(applicationService.addApplicationToCollectionIfMissing).toHaveBeenCalledWith(
        applicationCollection,
        ...additionalApplications.map(expect.objectContaining),
      );
      expect(comp.applicationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Domain query and add missing value', () => {
      const recruiter: IRecruiter = { id: 456 };
      const operationalDomains: IDomain[] = [{ id: 23118 }];
      recruiter.operationalDomains = operationalDomains;

      const domainCollection: IDomain[] = [{ id: 2654 }];
      jest.spyOn(domainService, 'query').mockReturnValue(of(new HttpResponse({ body: domainCollection })));
      const additionalDomains = [...operationalDomains];
      const expectedCollection: IDomain[] = [...additionalDomains, ...domainCollection];
      jest.spyOn(domainService, 'addDomainToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recruiter });
      comp.ngOnInit();

      expect(domainService.query).toHaveBeenCalled();
      expect(domainService.addDomainToCollectionIfMissing).toHaveBeenCalledWith(
        domainCollection,
        ...additionalDomains.map(expect.objectContaining),
      );
      expect(comp.domainsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const recruiter: IRecruiter = { id: 456 };
      const relatedUser: IProfile = { id: 30914 };
      recruiter.relatedUser = relatedUser;
      const wallet: IWallet = { id: 28749 };
      recruiter.wallet = wallet;
      const applications: IApplication = { id: 21574 };
      recruiter.applications = [applications];
      const operationalDomain: IDomain = { id: 6229 };
      recruiter.operationalDomains = [operationalDomain];

      activatedRoute.data = of({ recruiter });
      comp.ngOnInit();

      expect(comp.relatedUsersCollection).toContain(relatedUser);
      expect(comp.walletsCollection).toContain(wallet);
      expect(comp.applicationsSharedCollection).toContain(applications);
      expect(comp.domainsSharedCollection).toContain(operationalDomain);
      expect(comp.recruiter).toEqual(recruiter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecruiter>>();
      const recruiter = { id: 123 };
      jest.spyOn(recruiterFormService, 'getRecruiter').mockReturnValue(recruiter);
      jest.spyOn(recruiterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recruiter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recruiter }));
      saveSubject.complete();

      // THEN
      expect(recruiterFormService.getRecruiter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(recruiterService.update).toHaveBeenCalledWith(expect.objectContaining(recruiter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecruiter>>();
      const recruiter = { id: 123 };
      jest.spyOn(recruiterFormService, 'getRecruiter').mockReturnValue({ id: null });
      jest.spyOn(recruiterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recruiter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recruiter }));
      saveSubject.complete();

      // THEN
      expect(recruiterFormService.getRecruiter).toHaveBeenCalled();
      expect(recruiterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecruiter>>();
      const recruiter = { id: 123 };
      jest.spyOn(recruiterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recruiter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recruiterService.update).toHaveBeenCalled();
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

    describe('compareApplication', () => {
      it('Should forward to applicationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(applicationService, 'compareApplication');
        comp.compareApplication(entity, entity2);
        expect(applicationService.compareApplication).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDomain', () => {
      it('Should forward to domainService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(domainService, 'compareDomain');
        comp.compareDomain(entity, entity2);
        expect(domainService.compareDomain).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
