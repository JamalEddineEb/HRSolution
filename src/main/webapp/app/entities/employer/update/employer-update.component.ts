import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProfile } from 'app/entities/profile/profile.model';
import { ProfileService } from 'app/entities/profile/service/profile.service';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { WalletService } from 'app/entities/wallet/service/wallet.service';
import { EmployerService } from '../service/employer.service';
import { IEmployer } from '../employer.model';
import { EmployerFormService, EmployerFormGroup } from './employer-form.service';

@Component({
  standalone: true,
  selector: 'jhi-employer-update',
  templateUrl: './employer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmployerUpdateComponent implements OnInit {
  isSaving = false;
  employer: IEmployer | null = null;

  relatedUsersCollection: IProfile[] = [];
  walletsCollection: IWallet[] = [];

  protected employerService = inject(EmployerService);
  protected employerFormService = inject(EmployerFormService);
  protected profileService = inject(ProfileService);
  protected walletService = inject(WalletService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmployerFormGroup = this.employerFormService.createEmployerFormGroup();

  compareProfile = (o1: IProfile | null, o2: IProfile | null): boolean => this.profileService.compareProfile(o1, o2);

  compareWallet = (o1: IWallet | null, o2: IWallet | null): boolean => this.walletService.compareWallet(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employer }) => {
      this.employer = employer;
      if (employer) {
        this.updateForm(employer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employer = this.employerFormService.getEmployer(this.editForm);
    if (employer.id !== null) {
      this.subscribeToSaveResponse(this.employerService.update(employer));
    } else {
      this.subscribeToSaveResponse(this.employerService.create(employer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(employer: IEmployer): void {
    this.employer = employer;
    this.employerFormService.resetForm(this.editForm, employer);

    this.relatedUsersCollection = this.profileService.addProfileToCollectionIfMissing<IProfile>(
      this.relatedUsersCollection,
      employer.relatedUser,
    );
    this.walletsCollection = this.walletService.addWalletToCollectionIfMissing<IWallet>(this.walletsCollection, employer.wallet);
  }

  protected loadRelationshipsOptions(): void {
    this.profileService
      .query({ filter: 'employer-is-null' })
      .pipe(map((res: HttpResponse<IProfile[]>) => res.body ?? []))
      .pipe(
        map((profiles: IProfile[]) => this.profileService.addProfileToCollectionIfMissing<IProfile>(profiles, this.employer?.relatedUser)),
      )
      .subscribe((profiles: IProfile[]) => (this.relatedUsersCollection = profiles));

    this.walletService
      .query({ filter: 'employer-is-null' })
      .pipe(map((res: HttpResponse<IWallet[]>) => res.body ?? []))
      .pipe(map((wallets: IWallet[]) => this.walletService.addWalletToCollectionIfMissing<IWallet>(wallets, this.employer?.wallet)))
      .subscribe((wallets: IWallet[]) => (this.walletsCollection = wallets));
  }
}
