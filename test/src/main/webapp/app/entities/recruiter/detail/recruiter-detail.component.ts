import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRecruiter } from '../recruiter.model';

@Component({
  standalone: true,
  selector: 'jhi-recruiter-detail',
  templateUrl: './recruiter-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RecruiterDetailComponent {
  recruiter = input<IRecruiter | null>(null);

  previousState(): void {
    window.history.back();
  }
}
