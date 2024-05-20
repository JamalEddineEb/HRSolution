import { IProfile, NewProfile } from './profile.model';

export const sampleWithRequiredData: IProfile = {
  id: 7262,
  role: 'EMPLOYER',
  status: 'DEACTIVATED',
};

export const sampleWithPartialData: IProfile = {
  id: 21564,
  profileImage: '../fake-data/blob/hipster.png',
  profileImageContentType: 'unknown',
  address: 'meanwhile oh',
  role: 'EMPLOYER',
  status: 'BANNED',
};

export const sampleWithFullData: IProfile = {
  id: 1360,
  profileImage: '../fake-data/blob/hipster.png',
  profileImageContentType: 'unknown',
  address: 'ah',
  role: 'RECRUITER',
  status: 'DEACTIVATED',
};

export const sampleWithNewData: NewProfile = {
  role: 'RECRUITER',
  status: 'DEACTIVATED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
