import { ICandidate, NewCandidate } from './candidate.model';

export const sampleWithRequiredData: ICandidate = {
  id: 29709,
  fullName: 'along interestingly ha',
  yearsOfExperience: 16823,
  hasContract: false,
  hired: false,
};

export const sampleWithPartialData: ICandidate = {
  id: 11171,
  fullName: 'who recap delightfully',
  yearsOfExperience: 27939,
  hasContract: true,
  hired: true,
};

export const sampleWithFullData: ICandidate = {
  id: 11415,
  linkedinUrl: 'forenenst downfall',
  fullName: 'froth dovetail nervously',
  yearsOfExperience: 18203,
  currentSalary: 9970.5,
  desiredSalary: 677.62,
  hasContract: false,
  hired: false,
  rate: 12849.63,
};

export const sampleWithNewData: NewCandidate = {
  fullName: 'expedite winds bit',
  yearsOfExperience: 21772,
  hasContract: true,
  hired: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
