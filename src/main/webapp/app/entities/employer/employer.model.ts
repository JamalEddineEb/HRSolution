import { IProfile } from 'app/entities/profile/profile.model';
import { IWallet } from 'app/entities/wallet/wallet.model';

export interface IEmployer {
  id: number;
  label?: string | null;
  linkedinUrl?: string | null;
  score?: number | null;
  relatedUser?: IProfile | null;
  wallet?: IWallet | null;
}

export type NewEmployer = Omit<IEmployer, 'id'> & { id: null };
