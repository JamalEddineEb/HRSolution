import { IUser } from 'app/entities/user/user.model';
import { IWallet } from 'app/entities/wallet/wallet.model';

export interface IEmployer {
  id: number;
  label?: string | null;
  linkedinUrl?: string | null;
  score?: number | null;
  internalUser?: Pick<IUser, 'id'> | null;
  wallet?: IWallet | null;
}

export type NewEmployer = Omit<IEmployer, 'id'> & { id: null };
