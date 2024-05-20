import { IProfile } from 'app/entities/profile/profile.model';
import { IWallet } from 'app/entities/wallet/wallet.model';

export interface IAdmin {
  id: number;
  systemName?: string | null;
  relatedUser?: IProfile | null;
  systemWallet?: IWallet | null;
}

export type NewAdmin = Omit<IAdmin, 'id'> & { id: null };
