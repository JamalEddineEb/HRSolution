import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IWallet } from '../wallet.model';

@Component({
  standalone: true,
  selector: 'jhi-wallet-detail',
  templateUrl: './wallet-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class WalletDetailComponent {
  wallet = input<IWallet | null>(null);

  previousState(): void {
    window.history.back();
  }
}
