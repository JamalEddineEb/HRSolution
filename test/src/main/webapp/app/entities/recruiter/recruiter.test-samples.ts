import { IRecruiter, NewRecruiter } from './recruiter.model';

export const sampleWithRequiredData: IRecruiter = {
  id: 14954,
  label: 'notepad society indeed',
  linkedinUrl: 'besides expense instructive',
};

export const sampleWithPartialData: IRecruiter = {
  id: 4466,
  label: 'meanwhile because',
  linkedinUrl: 'cruelly empty',
  approvedExperience: false,
};

export const sampleWithFullData: IRecruiter = {
  id: 14382,
  label: 'platter',
  linkedinUrl: 'affair hosiery',
  approvedExperience: false,
  score: 30416.82,
};

export const sampleWithNewData: NewRecruiter = {
  label: 'sans absent staid',
  linkedinUrl: 'now',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
