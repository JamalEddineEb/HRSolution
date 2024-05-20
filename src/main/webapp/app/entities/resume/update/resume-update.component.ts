import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ResumeService } from '../service/resume.service';
import { IResume } from '../resume.model';
import { ResumeFormService, ResumeFormGroup } from './resume-form.service';

@Component({
  standalone: true,
  selector: 'jhi-resume-update',
  templateUrl: './resume-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ResumeUpdateComponent implements OnInit {
  isSaving = false;
  resume: IResume | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected resumeService = inject(ResumeService);
  protected resumeFormService = inject(ResumeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ResumeFormGroup = this.resumeFormService.createResumeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resume }) => {
      this.resume = resume;
      if (resume) {
        this.updateForm(resume);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('hrSolutionApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resume = this.resumeFormService.getResume(this.editForm);
    if (resume.id !== null) {
      this.subscribeToSaveResponse(this.resumeService.update(resume));
    } else {
      this.subscribeToSaveResponse(this.resumeService.create(resume));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResume>>): void {
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

  protected updateForm(resume: IResume): void {
    this.resume = resume;
    this.resumeFormService.resetForm(this.editForm, resume);
  }
}
