import { IApplicationUser, NewApplicationUser } from './application-user.model';

export const sampleWithRequiredData: IApplicationUser = {
  id: 2092,
  role: 'EMPLOYER',
  status: 'ACTIVE',
};

export const sampleWithPartialData: IApplicationUser = {
  id: 14958,
  role: 'ADMIN',
  status: 'ACTIVE',
};

export const sampleWithFullData: IApplicationUser = {
  id: 4209,
  profileImage: '../fake-data/blob/hipster.png',
  profileImageContentType: 'unknown',
  address: 'when by unused',
  role: 'RECRUITER',
  status: 'ACTIVE',
};

export const sampleWithNewData: NewApplicationUser = {
  role: 'RECRUITER',
  status: 'BANNED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
