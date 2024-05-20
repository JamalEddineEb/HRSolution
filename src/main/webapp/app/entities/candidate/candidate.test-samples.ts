import { ICandidate, NewCandidate } from './candidate.model';

export const sampleWithRequiredData: ICandidate = {
  id: 21851,
  fullName: 'strum arise',
  yearsOfExperience: 5623,
  resume: '../fake-data/blob/hipster.png',
  resumeContentType: 'unknown',
  hasContract: true,
  hired: true,
};

export const sampleWithPartialData: ICandidate = {
  id: 20849,
  fullName: 'neighboring whoa',
  yearsOfExperience: 28996,
  resume: '../fake-data/blob/hipster.png',
  resumeContentType: 'unknown',
  hasContract: true,
  hired: false,
};

export const sampleWithFullData: ICandidate = {
  id: 22587,
  linkedinUrl: 'however late usually',
  fullName: 'past',
  yearsOfExperience: 12338,
  resume: '../fake-data/blob/hipster.png',
  resumeContentType: 'unknown',
  currentSalary: 14784.5,
  desiredSalary: 28755.31,
  hasContract: true,
  hired: true,
  rate: 12161.96,
};

export const sampleWithNewData: NewCandidate = {
  fullName: 'pish',
  yearsOfExperience: 11103,
  resume: '../fake-data/blob/hipster.png',
  resumeContentType: 'unknown',
  hasContract: false,
  hired: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
