import { IUser } from 'app/entities/user/user.model';
import { UserRole } from 'app/entities/enumerations/user-role.model';
import { UserStatus } from 'app/entities/enumerations/user-status.model';

export interface IApplicationUser {
  id: number;
  profileImage?: string | null;
  profileImageContentType?: string | null;
  address?: string | null;
  role?: keyof typeof UserRole | null;
  status?: keyof typeof UserStatus | null;
  internalUser?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewApplicationUser = Omit<IApplicationUser, 'id'> & { id: null };
