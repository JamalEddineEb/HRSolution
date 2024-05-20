import { IProfile } from 'app/entities/profile/profile.model';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { IApplication } from 'app/entities/application/application.model';
import { IDomain } from 'app/entities/domain/domain.model';

export interface IRecruiter {
  id: number;
  label?: string | null;
  linkedinUrl?: string | null;
  approvedExperience?: boolean | null;
  score?: number | null;
  relatedUser?: IProfile | null;
  wallet?: IWallet | null;
  applications?: IApplication[] | null;
  operationalDomains?: IDomain[] | null;
}

export type NewRecruiter = Omit<IRecruiter, 'id'> & { id: null };
