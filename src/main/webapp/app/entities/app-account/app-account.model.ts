import dayjs from 'dayjs/esm';
import { IProfile } from 'app/entities/profile/profile.model';
import { IAppAccountType } from 'app/entities/app-account-type/app-account-type.model';
import { IProvider } from 'app/entities/provider/provider.model';
import { IEmployer } from 'app/entities/employer/employer.model';

export interface IAppAccount {
  id: number;
  accountNumber?: number | null;
  cardNumber?: number | null;
  endDate?: dayjs.Dayjs | null;
  cvv?: number | null;
  owner?: IProfile | null;
  types?: IAppAccountType[] | null;
  providers?: IProvider[] | null;
  ifEmployer?: IEmployer | null;
}

export type NewAppAccount = Omit<IAppAccount, 'id'> & { id: null };
