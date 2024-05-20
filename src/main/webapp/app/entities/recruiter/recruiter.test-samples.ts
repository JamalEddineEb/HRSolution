import { IRecruiter, NewRecruiter } from './recruiter.model';

export const sampleWithRequiredData: IRecruiter = {
  id: 11889,
  label: 'eek inasmuch',
  linkedinUrl: 'indeed well bonnet',
};

export const sampleWithPartialData: IRecruiter = {
  id: 5746,
  label: 'duh chandelier',
  linkedinUrl: 'patrol wick while',
  approvedExperience: false,
  score: 7627.23,
};

export const sampleWithFullData: IRecruiter = {
  id: 30315,
  label: 'glorify while geez',
  linkedinUrl: 'disinter a searchingly',
  approvedExperience: true,
  score: 16465.64,
};

export const sampleWithNewData: NewRecruiter = {
  label: 'despite onto',
  linkedinUrl: 'down defenseless blond',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
