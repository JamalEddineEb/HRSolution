import { IEmployer, NewEmployer } from './employer.model';

export const sampleWithRequiredData: IEmployer = {
  id: 4704,
  label: 'expensive',
};

export const sampleWithPartialData: IEmployer = {
  id: 1865,
  label: 'prune whoa',
  linkedinUrl: 'tear hence',
  score: 15096.24,
};

export const sampleWithFullData: IEmployer = {
  id: 3273,
  label: 'parallel grammar',
  linkedinUrl: 'provided cautiously fright',
  score: 21290.54,
};

export const sampleWithNewData: NewEmployer = {
  label: 'now hence eek',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
