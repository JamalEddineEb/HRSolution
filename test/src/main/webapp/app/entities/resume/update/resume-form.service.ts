import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IResume, NewResume } from '../resume.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResume for edit and NewResumeFormGroupInput for create.
 */
type ResumeFormGroupInput = IResume | PartialWithRequiredKeyOf<NewResume>;

type ResumeFormDefaults = Pick<NewResume, 'id'>;

type ResumeFormGroupContent = {
  id: FormControl<IResume['id'] | NewResume['id']>;
  file: FormControl<IResume['file']>;
  fileContentType: FormControl<IResume['fileContentType']>;
  filename: FormControl<IResume['filename']>;
  owner: FormControl<IResume['owner']>;
};

export type ResumeFormGroup = FormGroup<ResumeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResumeFormService {
  createResumeFormGroup(resume: ResumeFormGroupInput = { id: null }): ResumeFormGroup {
    const resumeRawValue = {
      ...this.getFormDefaults(),
      ...resume,
    };
    return new FormGroup<ResumeFormGroupContent>({
      id: new FormControl(
        { value: resumeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      file: new FormControl(resumeRawValue.file, {
        validators: [Validators.required],
      }),
      fileContentType: new FormControl(resumeRawValue.fileContentType),
      filename: new FormControl(resumeRawValue.filename, {
        validators: [Validators.required],
      }),
      owner: new FormControl(resumeRawValue.owner),
    });
  }

  getResume(form: ResumeFormGroup): IResume | NewResume {
    return form.getRawValue() as IResume | NewResume;
  }

  resetForm(form: ResumeFormGroup, resume: ResumeFormGroupInput): void {
    const resumeRawValue = { ...this.getFormDefaults(), ...resume };
    form.reset(
      {
        ...resumeRawValue,
        id: { value: resumeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ResumeFormDefaults {
    return {
      id: null,
    };
  }
}
